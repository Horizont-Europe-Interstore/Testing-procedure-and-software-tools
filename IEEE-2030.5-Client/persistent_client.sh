#!/bin/bash

# Command file for external communication
COMMAND_FILE="/tmp/ieee2030_commands"
PID_FILE="/tmp/ieee2030_client.pid"

# Auto-detect network interface
echo "Detecting network interfaces..."
INTERFACE=$(/app/build/client_test 2>&1 | grep -E '^[0-9]+:' | head -1 | cut -d':' -f2 | awk '{print $1}')

if [ -z "$INTERFACE" ]; then
  echo "No network interface detected, trying common names..."
  for iface in eth0 ens160 enp0s3 wlan0; do
    if ip link show $iface >/dev/null 2>&1; then
      INTERFACE=$iface
      break
    fi
  done
fi

if [ -z "$INTERFACE" ]; then
  echo "Error: Could not detect network interface"
  exit 1
fi

# Default values
DEVICE_CERT="${DEVICE_CERT:-pti_dev.x509}"
CA_CERT="${CA_CERT:-./certs/my_ca.pem}"
SERVER_URL="${SERVER_URL:-https://xxx.xxx.xxx.xxx:8443/}"

echo "IEEE 2030.5 Client Thread Started"
echo "Interface: $INTERFACE"
echo "Device Cert: $DEVICE_CERT"
echo "CA Cert: $CA_CERT"
echo "Server URL: $SERVER_URL"
echo "Command file: $COMMAND_FILE"
echo "========================"

# Create command file if it doesn't exist
touch $COMMAND_FILE
echo $$ > $PID_FILE

# Function to execute client with URL
execute_url() {
  local url="$1"
  echo "[$(date)] Executing URL: $url"
  /app/build/client_test $INTERFACE $DEVICE_CERT $CA_CERT $url
  echo "[$(date)] Execution completed with exit code: $?"
}

# Execute initial connection to server
if [ -n "$SERVER_URL" ]; then
  execute_url "$SERVER_URL"
fi

# Main loop - check for commands
while true; do
  if [ -s "$COMMAND_FILE" ]; then
    # Read and execute URLs
    while IFS= read -r url; do
      if [ -n "$url" ]; then
        execute_url "$url"
      fi
    done < "$COMMAND_FILE"
    
    # Clear command file after processing
    > "$COMMAND_FILE"
  fi
  
  # Check every 2 seconds for new commands
  sleep 2
done
