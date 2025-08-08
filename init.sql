CREATE TABLE IF NOT EXISTS profiles (
    userId INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    dateOfBirth DATETIME NOT NULL,
    gender ENUM('MALE', 'FEMALE', 'OTHER'),
    subscribedMarketing BOOLEAN DEFAULT NULL,
    hasSetupPreference BOOLEAN DEFAULT NULL
);

-- Thêm một số dữ liệu mẫu vào bảng profiles.
INSERT INTO profiles (username, dateOfBirth, gender, subscribedMarketing, hasSetupPreference)
VALUES
    ('ekin_nguyen', '1989-06-11', 'MALE', true, true);
