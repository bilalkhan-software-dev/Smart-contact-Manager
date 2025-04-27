#!/bin/sh

# Install Amazon Corretto 21 (verified working JDK)
apt-get update && apt-get install -y wget
wget https://corretto.aws/downloads/latest/amazon-corretto-21-x64-linux-jdk.deb
dpkg -i amazon-corretto-21-x64-linux-jdk.deb

# Verify installation
/usr/lib/jvm/amazon-corretto-21-jdk-amd64/bin/java -version