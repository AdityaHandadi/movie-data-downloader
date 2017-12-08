# movie-data-downloader

## Prerequisites
Install Aws CLI and configure AWS CLI using below command.

```
aws configure
```
Provide your credentials, mention region(ex: us-east-1) and output format(ex: json)

Once above step is successful the credentials file would have created and updated in `user.home` 's `/.aws/credentials` 

## Properties

The following properties are important:
`aws.s3.bucket` : which S3 bucket the file needs to be downloaded from
`aws.file.path` : file location path in S3
`download.file.name` : name of the file that needs to be downloaded. 
`download.output.file.path` : location in your local machine where the file need to be downloaded. A new file would be created 
with the same name as _download.file.name_

These abovementioned properties need to be set as System args. An example of VM args required for the program is below:
```
-Daws.s3.bucket=imdb-datasets
-Daws.file.path=documents/v1/current/
-Ddownload.file.name=title.principals.tsv.gz
-Ddownload.output.file.path=/Users/adityahandadi/Documents/AWS/
```

NOTE: For the `movie-api` to run 100% you will need these three files to download - `name.basics.tsv.gz`, `title.basics.tsv.gz`, 
`title.principals.tsv.gz`.
