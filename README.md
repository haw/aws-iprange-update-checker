# aws-iprange-update-checker
This is the AWS Lambda Function for checking update of the range of AWS IP.

## Requirements

* Java 8+
* Gradle 

## Configuration

Rename src/main/resources/aws.properties.sample to src/main/resources/aws.properties.sample.  
And setting following values.

* s3.endpoint  
Specify S3 endpoint. ex, s3-ap-northeast-1.amazonaws.com  
* s3.bucketname
Specify S3 bucket name. This bucket used to store checked ip-range.json file.  
* sns.region  
Specify SNS region.  
* sns.topic.arn  
Specify SNS topic arn. If the IP ranges updated, this arn used to notify the diff.  
* access.key(optional)  
Specify the aws access key.  
* secret.key(optional)
Specify the aws secret key.

If access.key does not specified, use IAM Role.

### IAM Role Permission  

IAM Role needs following permissions.

* S3 get, put permissions.  
* SNS publish permission.

## Build

Exec following command.

  ```groovy
  gradle build
  ``` 

When the build is finished, the jar is generated in the build/libs, please use it as a Lambda Function code.

## License

The MIT License (MIT)

Copyright (c) 2015 HAW International Inc.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.



