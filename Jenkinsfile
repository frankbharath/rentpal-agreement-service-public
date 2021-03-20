pipeline{
    agent none
    environment {
        AWS_ACCESS_KEY_ID     = credentials('access-key-id')
        AWS_SECRET_ACCESS_KEY = credentials('secret-access-key')
        ARTIFACT_NAME = 'agreement-service.jar'
        AWS_S3_BUCKET = 'rentpal-spring-boot'
        AWS_EB_APP_NAME = 'rentpal-eb'
        AWS_EB_ENVIRONMENT = 'Rentpal-eb-env'
        AWS_EB_APP_VERSION = "${BUILD_ID}"
        EMAIL_TO = 'rentpal@zohomail.eu'
    }
    stages{
        stage('Build'){
            agent { dockerfile {filename 'Dockerfile' args '-u root --entrypoint ""' } }
            steps{
               sh 'mvn clean package -Pprod'
            }
        }
        stage ('Archive'){
            agent any
            steps{
                archiveArtifacts 'target/*.jar'
            }
        }
        stage('Deploy'){
            agent any
            steps{
                sh 'aws configure set region us-east-2'
                sh 'aws s3 cp ./target/agreement-service.jar s3://rentpal-spring-boot/agreement-service.jar'
                sh 'aws elasticbeanstalk --debug create-application-version --application-name $AWS_EB_APP_NAME --version-label $AWS_EB_APP_VERSION --source-bundle S3Bucket=$AWS_S3_BUCKET,S3Key=$ARTIFACT_NAME'
                sh 'aws elasticbeanstalk update-environment --application-name $AWS_EB_APP_NAME --environment-name $AWS_EB_ENVIRONMENT --version-label $AWS_EB_APP_VERSION'
            }
        }
    }
    post {
        always {
            node('master') {
                cleanWs()
            }
        }
    }
}
