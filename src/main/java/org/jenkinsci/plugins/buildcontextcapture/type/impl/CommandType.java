package org.jenkinsci.plugins.buildcontextcapture.type.impl;

import com.sun.xml.internal.rngom.ast.builder.BuildException;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.Node;
import hudson.model.TaskListener;
import hudson.remoting.Callable;
import hudson.tasks.BatchFile;
import hudson.tasks.CommandInterpreter;
import hudson.tasks.Shell;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
import org.jenkinsci.plugins.buildcontextcapture.service.EnvVarsGetter;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureType;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureTypeDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class CommandType extends BuildContextCaptureType {

    private String scriptContent;
    private String filePath;

    @DataBoundConstructor
    public CommandType(String scriptContent, String filePath) {
        this.scriptContent = scriptContent;
        this.filePath = filePath;
    }

    public String getScriptContent() {
        return scriptContent;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public void captureAndExport(AbstractBuild build, final TaskListener listener, final File outputCaptureDir, String format) throws BuildContextException {

        EnvVarsGetter envVarsGetter = new EnvVarsGetter();
        final Map<String, String> envVars = envVarsGetter.gatherJobEnvVars(build);
        final FilePath workspace = build.getWorkspace();
        try {
            workspace.mkdirs();
        } catch (IOException ioe) {
            throw new BuildContextException(ioe);
        } catch (InterruptedException ie) {
            throw new BuildContextException(ie);
        }

        Node node = build.getBuiltOn();
        if (node != null) {
            final FilePath rootPath = node.getRootPath();
            if (rootPath != null) {
                try {

                    boolean isUnix = rootPath.act(new Callable<Boolean, Throwable>() {
                        public Boolean call() throws Throwable {
                            return File.pathSeparatorChar == ':';
                        }
                    });

                    CommandInterpreter batchRunner;
                    if (isUnix) {
                        batchRunner = new Shell(scriptContent);
                    } else {
                        batchRunner = new BatchFile(scriptContent);
                    }
                    FilePath tmpFile;
                    try {
                        tmpFile = batchRunner.createScriptFile(rootPath);
                    } catch (IOException ioe) {
                        throw new BuildContextException(ioe);
                    } catch (InterruptedException ie) {
                        throw new BuildContextException(ie);
                    }
                    final String[] cmd = batchRunner.buildCommandLine(tmpFile);

                    FilePath generatedFile = rootPath.act(new Callable<FilePath, BuildContextException>() {
                        public FilePath call() throws BuildContextException {
                            Launcher launcher = new Launcher.LocalLauncher(listener);
                            try {
                                int exitCode = launcher.launch().cmds(cmd).envs(envVars).stdout(listener).pwd(workspace).join();
                                if (exitCode != 0) {
                                    throw new BuildContextException("The expected exit code is " + exitCode);
                                }

                            } catch (IOException ioe) {
                                throw new BuildContextException(ioe);
                            } catch (InterruptedException ie) {
                                throw new BuildContextException(ie);
                            }

                            FilePath createdFile = new FilePath(workspace, filePath);
                            try {
                                if (!createdFile.exists()) {
                                    throw new BuildContextException("The executed command doesn't create the expected file" + filePath);
                                }
                                return createdFile;
                            } catch (IOException ioe) {
                                throw new BuildException(ioe);
                            } catch (InterruptedException ie) {
                                throw new BuildException(ie);
                            }
                        }
                    });

                    //Execute on master
                    if (generatedFile != null) {
                        build.getWorkspace().copyRecursiveTo(generatedFile.getName(), new FilePath(outputCaptureDir));
                    }

                } catch (Throwable throwable) {
                    throw new BuildContextException(throwable);
                }

            }
        }
    }

    @Override
    public Map<String, ? extends Object> getCapturedElements(AbstractBuild build, TaskListener listener) throws BuildContextException {
        return null;
    }

    @Override
    protected String getFileName() {
        return null;
    }

    @Extension
    @SuppressWarnings("unused")
    public static class CommandTypeDescriptor extends BuildContextCaptureTypeDescriptor<CommandType> {

        @Override
        public Class<? extends BuildContextCaptureType> getType() {
            return CommandType.class;
        }

        @Override
        public String getDisplayName() {
            return "through a command";
        }

        @Override
        public String getLabel() {
            return "Capture " + getDisplayName();
        }
    }

}
