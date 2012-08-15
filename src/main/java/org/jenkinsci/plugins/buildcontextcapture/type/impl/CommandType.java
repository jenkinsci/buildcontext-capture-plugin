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
import org.jenkinsci.plugins.buildcontextcapture.BuildContextLogger;
import org.jenkinsci.plugins.buildcontextcapture.service.EnvVarsGetter;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureTypeDescriptor;
import org.jenkinsci.plugins.buildcontextcapture.type.FlexibleBuildContextCaptureType;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class CommandType extends FlexibleBuildContextCaptureType {

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
    public void captureAndExport(AbstractBuild build, FilePath outputDir, BuildContextLogger logger) throws BuildContextException {

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

                    if (filePath == null) {
                        logger.info("You have to give a captured file path");
                        return;
                    }

                    //Execute script
                    if (scriptContent != null) {
                        logger.info(String.format("Executing the captured script %s", scriptContent));
                        executeScript(logger, envVars, workspace, rootPath);
                    }

                    //Capture file
                    FilePath capturedFilePath = new FilePath(build.getWorkspace(), filePath);
                    if (!capturedFilePath.exists()) {
                        logger.info(String.format("The captured script doesn't exist. You have to generate it", capturedFilePath.getRemote()));
                        return;
                    }
                    build.getWorkspace().copyRecursiveTo(capturedFilePath.getName(), outputDir);

                } catch (Throwable throwable) {
                    throw new BuildContextException(throwable);
                }
            }
        }
    }

    private void executeScript(BuildContextLogger logger, final Map<String, String> envVars, final FilePath workspace, FilePath rootPath) throws Throwable {
        boolean isUnix = rootPath.act(new Callable<Boolean, Throwable>() {
            public Boolean call() throws Throwable {
                return File.pathSeparatorChar == ':';
            }
        });

        final TaskListener listener = logger.getListener();
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

        rootPath.act(new Callable<FilePath, BuildContextException>() {
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
    }

    @Override
    public FilePath getExportedFilePath(AbstractBuild build, BuildContextLogger logger) throws BuildContextException {

        FilePath initialFile = new FilePath(build.getWorkspace(), filePath);
        try {
            if (!initialFile.exists()) {
                throw new BuildContextException("The file " + initialFile.getRemote() + " must exist.");
            }
            FilePath outputDir = getExportedDir(build);
            return outputDir.child(initialFile.getName());
        } catch (IOException ioe) {
            throw new BuildContextException(ioe);
        } catch (InterruptedException ie) {
            throw new BuildContextException(ie);
        }
    }

    @Extension
    @SuppressWarnings("unused")
    public static class CommandTypeDescriptor extends BuildContextCaptureTypeDescriptor<CommandType> {

        @Override
        public Class<? extends FlexibleBuildContextCaptureType> getType() {
            return CommandType.class;
        }

        @Override
        public String getDisplayName() {
            return "Elements by a script execution";
        }

        @Override
        public String getLabel() {
            return "Capture " + getDisplayName();
        }
    }

}
