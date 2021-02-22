pipeline{
    agent any
    environment {
        AWS_ACCESS_KEY_ID     = credentials('jenkins-aws-secret-key-id')
        AWS_SECRET_ACCESS_KEY = credentials('jenkins-aws-secret-access-key')
        ARTIFACT_NAME = 'agreement-service.jar'
        AWS_S3_BUCKET = 'rentpal-spring-boot'
        AWS_EB_APP_NAME = 'rentpal-eb'
        AWS_EB_ENVIRONMENT = 'Rentpaleb-env'
        AWS_EB_APP_VERSION = "${BUILD_ID}"
        EMAIL_TO = 'rentpal@zohomail.eu'
    }
    stages{
        stage('Build'){
            steps{
                script {
                    try{
                         sh 'mvn clean install -Pprod'
                    }catch(Exception e){
                         error 'Build Error'
                    }
                }
            }
        }
        stage('Deploy'){
            steps{
                archiveArtifacts 'target/*.jar'
                sh 'aws configure set region us-east-2'
                sh 'aws s3 cp ./target/agreement-service.jar s3://rentpal-spring-boot/agreement-service.jar'
                sh 'aws elasticbeanstalk --debug create-application-version --application-name $AWS_EB_APP_NAME --version-label $AWS_EB_APP_VERSION --source-bundle S3Bucket=$AWS_S3_BUCKET,S3Key=$ARTIFACT_NAME'
                sh 'aws elasticbeanstalk update-environment --application-name $AWS_EB_APP_NAME --environment-name $AWS_EB_ENVIRONMENT --version-label $AWS_EB_APP_VERSION'
            }
        }
    }
    post {
        always {
            cleanWs()
        }
        failure {
            emailext body: 'Check console output at $BUILD_URL to view the results. \n\n ${CHANGES} \n\n -------------------------------------------------- \n${BUILD_LOG, maxLines=100, escapeHtml=false}', 
                    to: "${EMAIL_TO}", 
                    subject: 'Build failed in Jenkins: $PROJECT_NAME - #$BUILD_NUMBER'
        }
    }
}
