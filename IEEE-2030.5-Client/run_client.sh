#!/bin/bash

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
  echo "Available interfaces:"
  /app/build/client_test
  exit 1
fi

# Default values (can be overridden by environment variables)
DEVICE_CERT="${DEVICE_CERT:-pti_dev.x509}"
CA_CERT="${CA_CERT:-./certs/my_ca.pem}"
SERVER_URL="${SERVER_URL:-https://java-backend:8443/dcap}"
COMMAND=""

# Parse command line arguments
while [[ $# -gt 0 ]]; do
  case $1 in
    --cert)
      DEVICE_CERT="$2"
      shift 2
      ;;
    --ca)
      CA_CERT="$2"
      shift 2
      ;;
    --url)
      SERVER_URL="$2"
      shift 2
      ;;
    --cmd)
      COMMAND="$2"
      shift 2
      ;;
    *)
      # First positional argument is command
      if [ -z "$COMMAND" ]; then
        COMMAND="$1"
      fi
      shift
      ;;
  esac
done

# Default run interval (seconds)
RUN_INTERVAL="${RUN_INTERVAL:-300}"
RUN_ONCE="${RUN_ONCE:-false}"

echo "Running client_test with:"
echo "Interface: $INTERFACE"
echo "Device Cert: $DEVICE_CERT"
echo "CA Cert: $CA_CERT"
echo "Server URL: $SERVER_URL"
echo "Command: $COMMAND"
echo "Run interval: $RUN_INTERVAL seconds"
echo "Run once: $RUN_ONCE"
echo "========================"

run_client() {
  echo "[$(date)] Starting IEEE 2030.5 client..."
  if [ -n "$COMMAND" ]; then
    /app/build/client_test $INTERFACE $DEVICE_CERT $CA_CERT $SERVER_URL $COMMAND
  else
    /app/build/client_test $INTERFACE $DEVICE_CERT $CA_CERT $SERVER_URL
  fi
  echo "[$(date)] Client execution completed with exit code: $?"
}

if [ "$RUN_ONCE" = "true" ]; then
  run_client
else
  echo "Running in persistent mode..."
  while true; do
    run_client
    echo "[$(date)] Waiting $RUN_INTERVAL seconds before next run..."
    sleep $RUN_INTERVAL
  done
fi 
