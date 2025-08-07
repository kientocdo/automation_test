# Sử dụng image chính thức của Node.js
FROM node:16

# Đặt thư mục làm việc cho ứng dụng
WORKDIR /usr/src/app

# Sao chép package.json và package-lock.json vào container
COPY package*.json ./

# Cài đặt các dependencies
RUN npm install

# Sao chép mã nguồn vào container
COPY . .

# Expose port 3000 cho ứng dụng
EXPOSE 3000

# Lệnh để chạy ứng dụng Node.js
CMD ["node", "server.js"]
