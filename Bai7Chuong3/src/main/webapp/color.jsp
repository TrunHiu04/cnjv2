<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Chọn màu nền</title>
    <style>
        body {
            background-color: ${sessionScope.color};
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            margin: 0;
            color: #333;
            transition: background-color 0.5s ease;
        }

        h2 {
            margin-bottom: 20px;
            color: #222;
        }

        form {
            background: rgba(255, 255, 255, 0.8);
            padding: 20px 30px;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        }

        select, input[type="submit"] {
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
        }

        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: #45a049;
        }

        p {
            margin-top: 20px;
            font-size: 18px;
        }

        .error {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <h2>Chọn màu nền:</h2>

    <form action="color" method="post">
        <select name="color">
            <option value="white">Trắng</option>
            <option value="red">Đỏ</option>
            <option value="blue">Xanh dương</option>
            <option value="green">Xanh lá</option>
            <option value="yellow">Vàng</option>
        </select>
        <br>
        <input type="submit" value="Đổi màu" />
    </form>

    <c:if test="${not empty sessionScope.color}">
        <p>Màu nền hiện tại: <strong>${sessionScope.color}</strong></p>
    </c:if>

    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>
</body>
</html>
