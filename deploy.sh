echo "Building app..."
./mvnw clean package

echo "Deploy files to server..."
scp -r target/demo-swp.jar root@157.245.203.86:/var/www/demo-swp-be/

ssh root@157.245.203.86 <<EOF
pid=\$(sudo lsof -t -i :8080)

if [ -z "\$pid" ]; then
    echo "Start server..."
else
    echo "Restart server..."
    sudo kill -9 "\$pid"
fi
cd /var/www/demo-swp-be
java -jar demo-swp.jar
EOF
exit
echo "Done!"