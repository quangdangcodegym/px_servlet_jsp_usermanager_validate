<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>User Management Application</title>
</head>
<body>
<center>
    <h1>User Management</h1>
    <h2>
        <a href="/users">List All Users</a>
    </h2>
</center>
<div align="center">
    <form method="post">
        <table border="1" cellpadding="5">
            <caption>
                <h2>Add New User</h2>
            </caption>
            <input type = "hidden" name ="id" value="${user.getId()}"/>
            <tr>
                <th>User Name:</th>
                <td>
                    <input type="text" name="name" id="name" size="45" value ="${user.getName() }"/>
                </td>
            </tr>
            <tr>
                <th>User Email:</th>
                <td>
                    <input type="text" name="email" id="email" size="45" value ="${user.getEmail() }"/>
                </td>
            </tr>
            <tr>
                <th>User password:</th>
                <td>
                    <input type="password" name="password" id="password" size="45" value ="${user.getPassword() }"/>
                </td>
            </tr>
            <!-- <tr>
                <th>User country:</th>
                <td>
                    <input type="text" name="country" size="45" value ="..."/>
                </td>
            </tr> -->
            <tr>
                <th>Country:</th>
                <td>
                    <select name="country">
                    	<c:forEach var ="country" items="${applicationScope.listCountry}">
                    		<option value="${country.id}">
                    			<c:out value="${country.name }"></c:out>
                    		</option>
                    	</c:forEach>
                    </select>
                </td>
                
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="submit" value="Save"/>
                </td>
            </tr>
        </table>
    </form>
    <div>
    	${errors }
    </div>
</div>
</body>
</html>