<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<%@ include file="/WEB-INF/views/included/included_head.jsp" %> 
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/project/project-view.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/included/included-comment.css" />
<script src="${pageContext.request.contextPath}/resources/js/project/project-view.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/included/included-comment.js"></script>
<script>
var beforeparentSeq= '${beforeProject.seq}';
var afterparentSeq = '${afterProject.seq}';
</script>
</head>
<body>
	<div class="wrapper">
		<c:import url="../included/included_nav.jsp" charEncoding="UTF-8" />
		
		<div class="project-detail">
			<div class="project-head">
				<h1 class="project-name"><c:out value="${project.title}"/></h1>
				<div class="project-subinfo">
					<c:if test="${!empty project.date}"> 
						<a class="project-date">DATE : <c:out value="${project.date}"/></a> <br/>
					</c:if>
					<a class="project-sect">SECT : <c:out value="${project.sect}"/></a>
					,&nbsp
					<a class="project-hits">HITS : <c:out value="${project.hits}"/></a>
				</div>
			</div>

			<div class="project-line"></div>
				   			
			<div class="project-subinfo2">
				<c:if test="${!empty project.developer}"> 
					<a class="project-developer">by ${project.developer}</a>
				</c:if>
				<c:if test="${!empty project.sourcecode}">
					<a class="btn row-center project-source" target="_black" href="${project.sourcecode}">
						<img src="${pageContext.request.contextPath}/resources/image/btn_projectview_source.png" style="width:0.8rem; height:0.8rem; margin-right:0.1rem">
						SOURCE
					</a>
				</c:if>
			</div>
			
			<div class="project-content editor-contents">
				<c:if test="${project.video eq 'N'}">
					<img class="project-snapsht" src="${pageContext.request.contextPath}${project.snapsht}" >
				</c:if>
				<c:out value="${project.contents}" escapeXml="false"/>
				
				<c:if test="${!empty files}">
					<h3>첨부파일</h3>
					<div class="project-files">
						<c:forEach var="file" items="${files}">
							<fmt:formatNumber var="filesize" value="${file.size/(1024*1024)}" pattern="0.00"/>
							<div class="project-file">
								 <a onclick="downloadFile('${file.pathNm}')"> <c:out value="${file.realNm}"/> (<c:out value="${filesize}"/> MB)</a>
							</div>												
						</c:forEach>
					</div>
				</c:if>
			</div>
		
			<c:import url="../included/included_comment.jsp" charEncoding="UTF-8">
			   <c:param name = "perPgLine" value = "10" />
			   <c:param name = "boardType" value = "project" />
			   <c:param name = "boardSeq" value = "${project.seq}" />
			   <c:param name = "comtCnt" value = "${project.comtCnt}" />
			</c:import>
		</div>
		
		<c:import url="../included/included_footer.jsp" charEncoding="UTF-8" />
		
		<c:if test="${!empty beforeProject}">
			<div class="btn btn-project-before h-reverse" title="[${beforeProject.sect}] ${beforeProject.title}" style="background-image: url(${pageContext.request.contextPath}/resources/image/btn_project_arrow.png)"></div>
			<script>
			(function() {
			    $(".btn-project-before").tooltip({
			    	 tooltipClass: "btn-project-tooltip",
			    	 show : null,
			    	 hide : null,
			    });
			})();
			</script>
		</c:if>
		
		<c:if test="${!empty afterProject}">
			<div class="btn btn-project-next" title="[${afterProject.sect}] ${afterProject.title}" style="background-image: url(${pageContext.request.contextPath}/resources/image/btn_project_arrow.png)"></div>
			<script>
			(function() {
			    $(".btn-project-next").tooltip({
			    	 tooltipClass: "btn-project-tooltip",
			    	 show : null,
			    	 hide : null,
			    });
			})();
			</script>
		</c:if>
	</div>
	
</body>
</html>