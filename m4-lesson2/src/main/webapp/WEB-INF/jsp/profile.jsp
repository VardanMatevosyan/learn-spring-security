<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<title>Profile</title>
</head>
<body>
    <jsp:include page="layout.jsp" />
    <div class="container">
        <h1>Profile</h1>
    </div>

    <div class="container">
        Principal authorities: <sec:authentication property="principal.authorities"/>
        <br />
        Name: <sec:authentication property="name"/>
        <br />
        Authorities: <sec:authentication property="authorities"/>
        <br />
        <sec:authentication property="principal.username" var="curentUserName" scope="page"/>
        <c:if test="${curentUserName.startsWith('u')}">
            <div>User name starts with u</div>
        </c:if>
    </div>
</body>
</html>
