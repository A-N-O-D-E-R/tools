package com.anode.tool.commands.ssh;

import com.jcraft.jsch.UserInfo;

import lombok.AllArgsConstructor;

/**
 * Implementation of UserInfo for JSch SSH authentication.
 */
@AllArgsConstructor
public class User implements UserInfo {

    /**
     * The password for SSH authentication.
     */
    private String password;

    /**
     * Gets the password for authentication.
     * @return the password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Gets the passphrase for key-based authentication.
     * @return null (not used)
     */
    @Override
    public String getPassphrase() {
        return null;
    }

    /**
     * Prompts for a yes/no response.
     * @param str the prompt message
     * @return true (auto-accept)
     */
    @Override
    public boolean promptYesNo(String str) {
        return true;
    }

    /**
     * Prompts for a passphrase.
     * @param message the prompt message
     * @return true (auto-accept)
     */
    @Override
    public boolean promptPassphrase(String message) {
        return true;
    }

    /**
     * Prompts for a password.
     * @param message the prompt message
     * @return true (auto-accept)
     */
    @Override
    public boolean promptPassword(String message) {
        return true;
    }

    /**
     * Shows a message to the user.
     * @param message the message to show
     */
    @Override
    public void showMessage(String message) {
        // Not used
    }

}
