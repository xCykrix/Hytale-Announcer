
rm -r hytale-api
mkdir -p hytale-api
chmod +x hytale-downloader-linux-amd64
./hytale-downloader-linux-amd64 -download-path ./hytale-api/latest.zip

cd hytale-api
unzip latest.zip
cd ..