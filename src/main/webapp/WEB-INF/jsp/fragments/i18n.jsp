<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">
    const i18n = {}; // https://learn.javascript.ru/object
    <c:forEach var="key" items='${["common.deleted","common.saved","common.enabled","common.disabled","common.errorStatus","common.confirm","app.locale"]}'>
        i18n["${key}"] = "<spring:message code="${key}"/>";
    </c:forEach>
    i18n["addTitle"] = "<spring:message code="${param.entity}.add"/>";
    i18n["editTitle"] = "<spring:message code="${param.entity}.edit"/>";
</script>