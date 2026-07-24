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

import com.anode.tool.commands.ssh.SshCommand.SshCommandType;

/**
 * Abstract base class for SSH command arguments.
 */
public abstract class SshArgs {

    /**
     * Default session timeout in milliseconds (5 seconds).
     */
    private static final int DEFAULT_SESSION_TIMEOUT = 5 * 1000;

    /**
     * The SSH username.
     */
    private String userName;

    /**
     * The SSH user password.
     */
    private String userPassword;

    /**
     * The remote host name or IP address.
     */
    private String remoteHostName;

    /**
     * The session timeout in milliseconds.
     */
    private int sessionTimeout = DEFAULT_SESSION_TIMEOUT;

    /**
     * Gets the SSH username.
     * @return the username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the SSH username.
     * @param userName the username to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the SSH user password.
     * @return the password
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Sets the SSH user password.
     * @param userPassword the password to set
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    /**
     * Gets the remote host name.
     * @return the remote host name
     */
    public String getRemoteHostName() {
        return remoteHostName;
    }

    /**
     * Sets the remote host name.
     * @param remoteHostName the remote host name to set
     */
    public void setRemoteHostName(String remoteHostName) {
        this.remoteHostName = remoteHostName;
    }

    /**
     * Gets the session timeout in milliseconds.
     * @return the session timeout
     */
    public int getSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * Sets the session timeout in milliseconds.
     * @param sessionTimeout the timeout to set
     */
    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    /**
     * Gets the SSH command type for this arguments set.
     * @return the SSH command type
     */
    abstract SshCommandType getSshCommandType();
}
