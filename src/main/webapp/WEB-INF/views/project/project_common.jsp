<c:set var="thumbDir"><spring:eval expression="@location['project.thumb.dir.url']"/></c:set>
<c:set var="fileDir"><spring:eval expression="@location['project.file.dir.url']"/></c:set>
<c:set var="imageDir"><spring:eval expression="@location['project.image.dir.url']"/></c:set>
<c:set var="boardType"><spring:eval expression="@constant['board.type.id.project']"/></c:set>
<script>
var thumbDir = '<c:out value="${thumbDir}"/>';
</script>