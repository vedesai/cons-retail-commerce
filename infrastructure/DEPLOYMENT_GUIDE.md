# Deployment Guide - Retail Product Service

## Overview

This guide provides step-by-step instructions for deploying the Retail Product Service to AWS EC2 using Infrastructure as Code (CloudFormation) and AWS CodePipeline.

## Prerequisites

1. AWS Account with appropriate permissions
2. AWS CLI installed and configured
3. GitHub repository access
4. EC2 Key Pair created in AWS
5. VPC and Subnet IDs available

## Step 1: Deploy EC2 Infrastructure

### 1.1 Using AWS IaC MCP Server

The infrastructure can be deployed using the AWS IaC MCP server tools. The CloudFormation template is located at `infrastructure/ec2-infrastructure.yaml`.

### 1.2 Manual Deployment

```bash
aws cloudformation create-stack \
  --stack-name retail-product-service-infra \
  --template-body file://infrastructure/ec2-infrastructure.yaml \
  --parameters \
    ParameterKey=InstanceType,ParameterValue=t3.medium \
    ParameterKey=KeyPairName,ParameterValue=your-key-pair-name \
    ParameterKey=VpcId,ParameterValue=vpc-xxxxxxxxx \
    ParameterKey=SubnetId,ParameterValue=subnet-xxxxxxxxx \
    ParameterKey=AllowedCidr,ParameterValue=0.0.0.0/0 \
    ParameterKey=DatabaseUrl,ParameterValue=jdbc:postgresql://localhost:5432/retaildb \
    ParameterKey=DatabaseUsername,ParameterValue=postgres \
    ParameterKey=DatabasePassword,ParameterValue=your-db-password \
  --capabilities CAPABILITY_NAMED_IAM
```

### 1.3 Verify Infrastructure

```bash
# Check stack status
aws cloudformation describe-stacks --stack-name retail-product-service-infra

# Get outputs
aws cloudformation describe-stacks \
  --stack-name retail-product-service-infra \
  --query 'Stacks[0].Outputs'
```

## Step 2: Configure GitHub Access

### 2.1 Create GitHub Personal Access Token

1. Go to GitHub Settings > Developer settings > Personal access tokens
2. Generate a new token with `repo` scope
3. Save the token securely

### 2.2 Update CodePipeline Template

Update the `GitHubToken` parameter in `codepipeline.yaml` with your token.

## Step 3: Deploy CodePipeline

### 3.1 Get Infrastructure Outputs

```bash
# Get EC2 Instance ID
INSTANCE_ID=$(aws cloudformation describe-stacks \
  --stack-name retail-product-service-infra \
  --query 'Stacks[0].Outputs[?OutputKey==`InstanceId`].OutputValue' \
  --output text)

# Get Application Bucket Name
BUCKET_NAME=$(aws cloudformation describe-stacks \
  --stack-name retail-product-service-infra \
  --query 'Stacks[0].Outputs[?OutputKey==`ApplicationBucketName`].OutputValue' \
  --output text)
```

### 3.2 Deploy Pipeline

```bash
aws cloudformation create-stack \
  --stack-name retail-product-service-pipeline \
  --template-body file://infrastructure/codepipeline.yaml \
  --parameters \
    ParameterKey=GitHubOwner,ParameterValue=Deloitte-US-Consulting-DD \
    ParameterKey=GitHubRepo,ParameterValue=cons-retail-samplerepo \
    ParameterKey=GitHubBranch,ParameterValue=main \
    ParameterKey=GitHubToken,ParameterValue=your-github-token \
    ParameterKey=EC2InstanceId,ParameterValue=$INSTANCE_ID \
    ParameterKey=ApplicationBucketName,ParameterValue=$BUCKET_NAME \
  --capabilities CAPABILITY_NAMED_IAM
```

## Step 4: Initial Manual Deployment (Optional)

If you want to deploy manually before setting up the pipeline:

### 4.1 Build Application

```bash
mvn clean package -DskipTests
```

### 4.2 Upload to S3

```bash
aws s3 cp target/retail-product-service-1.0.0.jar \
  s3://$BUCKET_NAME/retail-product-service-1.0.0.jar
```

### 4.3 Deploy to EC2

```bash
# Create deployment package
cd infrastructure
zip -r deploy.zip appspec.yml scripts/ ../target/retail-product-service-1.0.0.jar

# Upload deployment package
aws s3 cp deploy.zip s3://$BUCKET_NAME/deploy.zip

# Create deployment
aws deploy create-deployment \
  --application-name retail-product-service-app \
  --deployment-group-name retail-product-service-dg \
  --s3-location bucket=$BUCKET_NAME,key=deploy.zip,bundleType=zip
```

## Step 5: Verify Deployment

### 5.1 Check Application Status

```bash
# SSH into EC2 instance
ssh -i your-key.pem ec2-user@<EC2_PUBLIC_IP>

# Check service status
sudo systemctl status retail-product-service

# Check logs
sudo journalctl -u retail-product-service -f
```

### 5.2 Test API

```bash
# Health check
curl http://<EC2_PUBLIC_IP>:8080/actuator/health

# Test product endpoint
curl http://<EC2_PUBLIC_IP>:8080/api/v1/products
```

## Step 6: Monitor Pipeline

### 6.1 View Pipeline Status

```bash
aws codepipeline get-pipeline-state \
  --name retail-product-service-pipeline
```

### 6.2 View Build Logs

Access CodeBuild logs in AWS Console or via CLI:

```bash
aws codebuild batch-get-builds \
  --ids <build-id>
```

## Troubleshooting

### Issue: CodeDeploy Agent Not Running

```bash
# SSH into EC2 instance
sudo service codedeploy-agent status
sudo service codedeploy-agent start
```

### Issue: Application Not Starting

```bash
# Check logs
sudo journalctl -u retail-product-service -n 100

# Check Java installation
java -version

# Check file permissions
ls -la /opt/retail-product-service/
```

### Issue: Database Connection Failed

- Verify database is accessible from EC2
- Check security group rules
- Verify database credentials in application-prod.yml

## Rollback Procedure

### Rollback via CodeDeploy

```bash
# List deployments
aws deploy list-deployments \
  --application-name retail-product-service-app \
  --deployment-group-name retail-product-service-dg

# Rollback to previous deployment
aws deploy create-deployment \
  --application-name retail-product-service-app \
  --deployment-group-name retail-product-service-dg \
  --revision revisionType=S3,s3Location={bucket=$BUCKET_NAME,key=previous-deploy.zip,bundleType=zip}
```

### Manual Rollback

```bash
# SSH into EC2
# Stop service
sudo systemctl stop retail-product-service

# Restore from backup
cp /opt/retail-product-service/retail-product-service-1.0.0.jar.backup.* \
   /opt/retail-product-service/retail-product-service-1.0.0.jar

# Start service
sudo systemctl start retail-product-service
```

## Maintenance

### Update Application

1. Push changes to GitHub
2. CodePipeline automatically triggers
3. Monitor pipeline execution
4. Verify deployment

### Update Infrastructure

```bash
aws cloudformation update-stack \
  --stack-name retail-product-service-infra \
  --template-body file://infrastructure/ec2-infrastructure.yaml \
  --parameters ...
```

## Security Considerations

1. **Secrets Management**: Use AWS Secrets Manager or Parameter Store for sensitive data
2. **Network Security**: Restrict security group rules to necessary IPs
3. **IAM Roles**: Follow principle of least privilege
4. **Monitoring**: Enable CloudWatch logging and monitoring
5. **Updates**: Regularly update dependencies and AMI

## Cost Optimization

1. Use appropriate instance types
2. Enable auto-scaling (future enhancement)
3. Use reserved instances for production
4. Monitor and optimize resource usage

## Support

For deployment issues, check:
- CloudFormation stack events
- CodePipeline execution logs
- CodeBuild build logs
- CodeDeploy deployment logs
- EC2 instance logs

