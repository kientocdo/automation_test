const express = require('express');
const mysql = require('mysql2');
const { body, validationResult } = require('express-validator');
const app = express();
const port = 3000;

// Lấy các thông tin kết nối từ môi trường Docker
const db = mysql.createConnection({
  host: process.env.DB_HOST || 'localhost',  // Lấy DB_HOST từ môi trường Docker
  user: process.env.DB_USER || 'root',
  password: process.env.DB_PASSWORD || 'rootuser',
  database: process.env.DB_NAME || 'profileDB',
});

// Middleware để parse JSON
app.use(express.json());

// Route GET / - Trang chủ của API
app.get('/', (req, res) => {
  res.send('Welcome to the API!');
});

// API POST /v1/profile: Tạo một profile mới
app.post('/v1/profile', [
  body('username').isString().notEmpty().withMessage('Username is required'),
  body('dateOfBirth').isISO8601().toDate().withMessage('Date of birth must be a valid date'),
  body('gender').isIn(['MALE', 'FEMALE', 'OTHER']).withMessage('Gender must be one of MALE, FEMALE, OTHER'),
  body('subscribedMarketing').isBoolean().optional().withMessage('Subscribed marketing must be a boolean')
], (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ errors: errors.array() });
  }

  const { username, dateOfBirth, gender, subscribedMarketing } = req.body;

  // Kiểm tra nếu username đã tồn tại
  const checkQuery = `SELECT * FROM profiles WHERE username = ?`;
  db.query(checkQuery, [username], (err, results) => {
    if (err) {
      console.error("Error checking username availability:", err);
      return res.status(500).send('Error checking username availability');
    }
    if (results.length > 0) {
      return res.status(400).json({
        errors: [
          {
            msg: 'Username already exists',
            path: 'username',
            location: 'body',
          }
        ]
      });
    }

    // Lưu dateOfBirth trực tiếp vào MySQL (không chuyển đổi múi giờ)
    const query = `
      INSERT INTO profiles (username, dateOfBirth, gender, subscribedMarketing)
      VALUES (?, ?, ?, ?)
    `;
    
    db.query(query, [username, dateOfBirth, gender, subscribedMarketing], (err, result) => {
      if (err) {
        console.error("Error inserting data:", err);
        return res.status(500).send('Error inserting data');
      }
      res.status(201).send({ userId: result.insertId });
    });
  });
});

// API GET /v1/profile/{userId}: Lấy thông tin profile theo userId
app.get('/v1/profile/:userId', (req, res) => {
  const { userId } = req.params;
  const query = `SELECT * FROM profiles WHERE userId = ?`;

  db.query(query, [userId], (err, results) => {
    if (err) {
      console.error("Error fetching data:", err);
      return res.status(500).send('Error fetching data');
    }
    if (results.length === 0) {
      return res.status(404).send('Profile not found');
    }

    const profile = results[0];
    let dateOfBirth = profile.dateOfBirth;

    // Chỉ lấy phần ngày (YYYY-MM-DD) từ dateOfBirth và bỏ phần giờ
    dateOfBirth = dateOfBirth.toISOString().split('T')[0]; // Lấy phần ngày (YYYY-MM-DD)

    // Cập nhật lại giá trị dateOfBirth trong response
    profile.dateOfBirth = dateOfBirth;

    res.status(200).send(profile);
  });
});

// Khởi động server
app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});
