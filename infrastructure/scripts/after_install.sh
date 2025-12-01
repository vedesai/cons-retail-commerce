#!/bin/bash
# After install script for CodeDeploy

set -e

echo "Starting after_install script..."

# Set proper permissions
chown -R ec2-user:ec2-user /opt/retail-product-service
chmod +x /opt/retail-product-service/*.sh 2>/dev/null || true
chmod 644 /opt/retail-product-service/*.jar 2>/dev/null || true

# Ensure log directory exists
mkdir -p /var/log/retail-product-service
chown ec2-user:ec2-user /var/log/retail-product-service

echo "after_install script completed successfully"

