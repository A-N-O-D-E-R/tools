package com.anode.tool.commands.ssh;
/*
 * Copyright (c) 2016, Orange
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.File;

import com.anode.tool.commands.ssh.SshCommand.SshCommandType;

/**
 * Arguments for SCP (Secure Copy) command execution.
 */
public class ScpArgs extends SshArgs {

    /**
     * The source file to copy.
     */
    private File sourceFile;

    /**
     * The destination file name or path.
     */
    private String destFileName;

    /**
     * Whether this is a fetch operation (reverse copy).
     */
    private boolean reverse = false;

    /**
     * Gets the source file.
     * @return the source file
     */
    public File getSourceFile() {
        return sourceFile;
    }

    /**
     * Sets the source file.
     * @param sourceFile the source file to set
     */
    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    /**
     * Gets the destination file name.
     * @return the destination file name
     */
    public String getDestFileName() {
        return destFileName;
    }

    /**
     * Sets the destination file name.
     * @param destFileName the destination file name to set
     */
    public void setDestFileName(String destFileName) {
        this.destFileName = destFileName;
    }

    /**
     * Checks if this is a fetch operation.
     * @return true if fetching from remote
     */
    public boolean getFetching() {
        return this.reverse;
    }

    /**
     * Sets whether this is a fetch operation.
     * @param fetch true for fetch (reverse), false for push
     */
    public void setFetching(boolean fetch) {
        this.reverse = fetch;
    }

    /**
     * Gets the SSH command type.
     * @return SCP command type
     */
    @Override
    SshCommandType getSshCommandType() {
        return SshCommandType.SCP;
    }
}
