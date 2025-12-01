#!/bin/bash
# Before install script for CodeDeploy

set -e

echo "Starting before_install script..."

# Stop the application if running
if systemctl is-active --quiet retail-product-service; then
    echo "Stopping retail-product-service..."
    systemctl stop retail-product-service
fi

# Backup existing application if it exists
if [ -f /opt/retail-product-service/retail-product-service-1.0.0.jar ]; then
    echo "Backing up existing application..."
    cp /opt/retail-product-service/retail-product-service-1.0.0.jar /opt/retail-product-service/retail-product-service-1.0.0.jar.backup.$(date +%Y%m%d%H%M%S)
fi

# Clean up old backups (keep last 5)
cd /opt/retail-product-service
ls -t retail-product-service-*.jar.backup.* 2>/dev/null | tail -n +6 | xargs rm -f || true

echo "before_install script completed successfully"

