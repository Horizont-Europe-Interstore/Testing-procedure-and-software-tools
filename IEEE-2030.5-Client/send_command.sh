#!/bin/bash

# Script to send URL to the persistent client
COMMAND_FILE="/tmp/ieee2030_commands"

if [ -z "$1" ]; then
  echo "Usage: $0 <full_url>"
  echo "Examples:"
  echo "  $0 https://192.168.1.100:8443/dcap"
  echo "  $0 https://192.168.1.100:8443/edev"
  echo "  $0 https://192.168.1.100:8443/tm"
  exit 1
fi

URL="$1"

# Send URL to execute
echo "$URL" >> $COMMAND_FILE
echo "URL '$URL' sent to IEEE 2030.5 client"