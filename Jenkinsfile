node {
    try {
        env.ARTIFACT_NAME='agreement-service.jar'
        env.ARTIFACT_NAME = 'agreement-service.jar'
        env.AWS_S3_BUCKET = 'rentpal-spring-boot'
        env.AWS_EB_APP_NAME = 'rentpal-eb'
        env.AWS_EB_ENVIRONMENT = 'Rentpal-eb-env'
        env.AWS_EB_APP_VERSION = "${BUILD_ID}"
        env.EMAIL_TO = 'rentpal@zohomail.eu'

        checkout scm

        docker.image('maven:3.6-jdk-11').inside {
            stage('Build') {
                sh 'mvn clean package -Pprod'
            }
            stage('Test') {
                sh 'mvn test'
            }
        }

        stage ('Archive'){
            archiveArtifacts 'target/*.jar'
        }

        stage('Deploy'){
            withCredentials([string(credentialsId: 'access-key-id', variable: 'AWS_ACCESS_KEY_ID'), string(credentialsId: 'secret-access-key', variable: 'AWS_SECRET_ACCESS_KEY')]) {
                sh 'aws configure set region us-east-2'
                sh 'aws s3 cp ./target/agreement-service.jar s3://${AWS_S3_BUCKET}/agreement-service.jar'
                sh 'aws elasticbeanstalk --debug create-application-version --application-name $AWS_EB_APP_NAME --version-label $AWS_EB_APP_VERSION --source-bundle S3Bucket=$AWS_S3_BUCKET,S3Key=$ARTIFACT_NAME'
                sh 'aws elasticbeanstalk update-environment --application-name $AWS_EB_APP_NAME --environment-name $AWS_EB_ENVIRONMENT --version-label $AWS_EB_APP_VERSION'
            }
        }
    }finally{
        cleanWs()
    }
}
