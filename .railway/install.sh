#!/bin/sh

# Force install Corretto JDK 21
apt-get update
apt-get install -y wget
wget -O corretto.deb https://corretto.aws/downloads/latest/amazon-corretto-21-x64-linux-jdk.deb
dpkg -i corretto.deb
rm corretto.deb

# Verify installation
/usr/lib/jvm/amazon-corretto-21-jdk-amd64/bin/java -version