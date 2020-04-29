const AWS = require('aws-sdk');
const dynamodb = new AWS.DynamoDB({apiVersion: '2012-08-10'});

exports.handler = (event, context, callback) => {
    console.log(event)
    AWS.config.update({region: 'REGION'});
    let tableName = process.env.TABLE_NAME
    var source = JSON.stringify(event['source']).replace(/['"]+/g, '')
    var detail = JSON.stringify(event['detail'])
    var detailType = JSON.stringify(event['detail-type']).replace(/['"]+/g, '')
    var resources = JSON.stringify(event['resources'])

    dynamodb.putItem({
            TableName: tableName,
            Item: {
                "sourceSor": {
                    S: source
                },
                "timestamp": {
                    S: new Date().toISOString()
                },
                "detail": {
                    S: detail
                },
                "detailType": {
                    S: detailType
                },
                "resources": {
                    S: resources
                }
            }
        }, function(err, data) {
            if (err) {
                console.log(err, err.stack);
                callback(null, {
                    statusCode: '500',
                    body: err
                });
            } else {
                callback(null, {
                    statusCode: '200',
                    body: 'Hello!'
                });
            }
        });
}
