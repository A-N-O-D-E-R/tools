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

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for checking SSH protocol acknowledgments.
 */
class SshAcknowledge {

    /**
     * Checks the SSH protocol acknowledgment from an input stream.
     * Acknowledgment values:
     *  - 0 for success
     *  - 1 for error
     *  - 2 for fatal error
     * @param in the SSH input stream to check
     * @throws SshException if an error acknowledgment is received
     */
    void checkAck(InputStream in) {
    int ackVal = getAckValue(in);
    if (ackVal != 0 && ackVal != 'C') {
      handleAcknowledgeError(in, ackVal);
    }
  }


    /**
     * Reads the acknowledgment value from an input stream.
     * @param in the input stream to read from
     * @return the acknowledgment value
     * @throws SshException if no acknowledgment is received
     */
    private int getAckValue(InputStream in) {
    try {
      return in.read();
    } catch (IOException e) {
      throw new SshException("No acknowlegement value received", e);
    }
  }

    /**
     * Handles an error acknowledgment by throwing an appropriate exception.
     * @param in the input stream to read error details from
     * @param ackVal the acknowledgment value indicating the error type
     * @throws SshException describing the acknowledgment error
     */
    private void handleAcknowledgeError(InputStream in, int ackVal) {
    switch (ackVal) {
      case -1:
        throwNoAck();
      case 1:
      case 2:
        throwAckErrorMessage(in);
      default:
        throwAckUnknownError();
    }
  }

    /**
     * Throws an exception for no acknowledgment received.
     * @throws SshException indicating no acknowledgment
     */
    private void throwNoAck() {
    throw new SshException("No acknowlegement received");
  }

    /**
     * Throws an exception with the error message from the acknowledgment.
     * @param in the input stream to read the error message from
     * @throws SshException with the error message
     */
    private void throwAckErrorMessage(InputStream in) {
    throw new SshException(String.format("Ssh acknowlegement error: %s", getAckMessage(in)));
  }

    /**
     * Reads the error message from an acknowledgment.
     * @param in the input stream to read from
     * @return the error message, or the exception message if reading fails
     */
    private String getAckMessage(InputStream in) {
    try {
      return tryToGetAckMessage(in);
    } catch(IOException e) {
      return e.getMessage();
    }
  }

    /**
     * Attempts to read the error message from the acknowledgment stream.
     * @param in the input stream to read from
     * @return the error message
     * @throws IOException if reading fails
     */
    private String tryToGetAckMessage(InputStream in) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    int c;

    do {
      c = in.read();
      stringBuilder.append((char) c);
    } while(c!='\n');

    return stringBuilder.toString();
  }

    /**
     * Throws an exception for an unknown acknowledgment.
     * @throws SshException indicating unknown acknowledgment
     */
    private void throwAckUnknownError() {
    throw new SshException("Unknown acknowlegement received");
  }
}
