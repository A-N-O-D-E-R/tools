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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class Scp {

  public void scpToRemote(ScpArgs scpArgs) {
    Ssh ssh = new Ssh(scpArgs);

    ssh.initSshChannel();
    ssh.sendScpHeader(scpArgs.getSourceFile());

    writeFile(ssh.getOutputStream(), scpArgs.getSourceFile());
    ssh.writeTerminateChar();

    ssh.disconnect();
  }

  public void scpFromRemote(ScpArgs scpArgs) {
    scpArgs.setFetching(true);
    Ssh ssh = new Ssh(scpArgs);
        try {
            ssh.initSshChannel();
            readFile(ssh.getInputStream(), ssh.getOutputStream(),scpArgs.getSourceFile());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ssh.disconnect();
        }
    }
    
  private void writeFile(OutputStream scpOutputStream, File file) {
    try {
      doWriteFile(file, scpOutputStream);
    } catch (IOException e) {
      throw new SshException("Failed to write file to scp output stream", e);
    }
  }

  private void doWriteFile(File file, OutputStream scpOutputStream) throws IOException {
    Files.copy(file.toPath(), scpOutputStream);
  }


  private void readFile(InputStream in, OutputStream out, File destFile) throws IOException {
        byte[] buf = new byte[1024];

        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();
         // read '0644 '
         in.read(buf, 0, 5);

         long filesize = 0L;
         while (true) {
             if (in.read(buf, 0, 1) < 0) {
                 // error
                 break;
             }
             if (buf[0] == ' ') break;
             filesize = filesize * 10L + (long) (buf[0] - '0');
         }

         String file = null;
         for (int i = 0; ; i++) {
             in.read(buf, i, 1);
             if (buf[i] == (byte) 0x0a) {
                 file = new String(buf, 0, i);
                 break;
             }
         }

         // send '\0'
         buf[0] = 0;
         out.write(buf, 0, 1);
         out.flush();

         // read a content of lfile
         FileOutputStream fos = new FileOutputStream(destFile);
         int foo;
         while (true) {
             if (buf.length < filesize) foo = buf.length;
             else foo = (int) filesize;
             foo = in.read(buf, 0, foo);
             if (foo < 0) {
                 // error
                 break;
             }
             fos.write(buf, 0, foo);
             filesize -= foo;
             if (filesize == 0L) break;
         }

         new SshAcknowledge().checkAck(in);


         // send '\0'
         buf[0] = 0;
         out.write(buf, 0, 1);
         out.flush();

         try {
             if (fos != null) fos.close();
         } catch (Exception ex) {
             System.out.println(ex);
         }
    }
}
