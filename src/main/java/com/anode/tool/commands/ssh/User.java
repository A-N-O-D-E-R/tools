package com.anode.tool.commands.ssh;

import com.jcraft.jsch.UserInfo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class User implements UserInfo {

    private String password;
    
    @Override
    public String getPassword() {
          return password;
    }
  
    @Override
    public String getPassphrase() {
      return null;
    }
  
    @Override
    public boolean promptYesNo(String str){
      return true;
    }
  
    @Override
    public boolean promptPassphrase(String message) {
      return true;
    }
  
    @Override
    public boolean promptPassword(String message){
      return true;
    }
  
    @Override
    public void showMessage(String message) {
      // Not used
    }
    
}
