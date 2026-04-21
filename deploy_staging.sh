#!/bin/sh

# Deploys the service with the new health check endpoint to staging

# Assuming necessary environment variables are set: STAGING_SERVER, SSH_KEY_PATH, etc.

SERVICE_NAME="my_service"
STAGING_SERVER="your.staging.server"
STAGING_PATH="/path/to/staging/deployment"
LOCAL_BUILD_PATH="./build/my_service"

echo "Building the service..."
# Your build command here, for example:
# npm run build

echo "Transferring the build to the staging server..."
scp -i "$SSH_KEY_PATH" -r "$LOCAL_BUILD_PATH" "$STAGING_SERVER:$STAGING_PATH"

echo "Restarting the service on the staging server..."
ssh -i "$SSH_KEY_PATH" $STAGING_SERVER << 'SSHCMDS'
  cd /path/to/staging/deployment/my_service
  # Restart command, for example if using pm2:
  # pm2 restart my_service
  echo "Service restarted."
SSHCMDS

echo 'DONE'
