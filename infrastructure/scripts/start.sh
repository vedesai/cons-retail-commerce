#!/bin/bash
# Start script for CodeDeploy

set -e

echo "Starting application..."

# Reload systemd daemon
systemctl daemon-reload

# Start the service
systemctl start retail-product-service

# Wait a moment and check status
sleep 5

if systemctl is-active --quiet retail-product-service; then
    echo "Application started successfully"
    systemctl status retail-product-service --no-pager
else
    echo "ERROR: Application failed to start"
    systemctl status retail-product-service --no-pager
    journalctl -u retail-product-service -n 50 --no-pager
    exit 1
fi

echo "start script completed successfully"

