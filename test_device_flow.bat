@echo off
echo Testing IEEE 2030.5 device flow...
echo.
echo Step 1: Getting Device Capability
curl -k -s https://localhost:443/dcap
echo.
echo.
echo Step 2: Getting End Device List
curl -k -s https://localhost:443/edev