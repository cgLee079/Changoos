<%@ page pageEncoding="UTF-8"%>
<html>
<head>
<%@ include file="/WEB-INF/views/included/included_head.jsp" %> 
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/project/project-upload.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/included/included-fileupload.css"/>
<script src="${pageContext.request.contextPath}/resources/js/project/project-upload.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/included/included-fileupload.js"></script>
</head>

<body>
	<div class="wrapper">
		<c:import url="../included/included_nav.jsp" charEncoding="UTF-8" />
				
		<div class="wrap-upload-form">
			<div class="upload-title">프로젝트 업로드</div>
			
			<form id="uploadForm" action="${pageContext.request.contextPath}/mgnt/project/upload.do" 
				method="post" enctype="multipart/form-data">
				
				<c:if test="${not empty project}">
					<input type="hidden" name="seq" value="<c:out value='${project.seq}'/>"/>
					<input type="hidden" name="snapsht" value="<c:out value='${project.snapsht}'/>"/>
					<input type="hidden" name="hits" value="<c:out value='${project.hits}'/>"/>
				</c:if>
				
				<div class="upload-project">
					<div class="upload-project-name">SECT</div>
					<div class="upload-project-input">
						<input type="text" id="sect" name="sect" class="project-sect" value="<c:out value='${project.sect}'/>"/>
					</div>
				</div>
				
				<div class="upload-project">
					<div class="upload-project-name">SOURCECODE</div>
					<div class="upload-project-input">
						<input type="text" id="sourcecode" name="sourcecode" class="project-sourcecode" value="<c:out value='${project.sourcecode}'/>"/>
					</div>
				</div>
				
					<div class="upload-project">
					<div class="upload-project-name">DATE</div>
					<div class="upload-project-input">
						<input type="text" id="date" name="date" class="project-date" value="<c:out value='${project.date}'/>"/>
					</div>
				</div>
				
				<div class="upload-project">
					<div class="upload-project-name">DEVELOPER</div>
					<div class="upload-project-input">
						<input type="text" id="developer" name="developer" class="project-developer" value="<c:out value='${project.developer}'/>"/>
					</div>
				</div>
				
				<div class="upload-project">
					<div class="upload-project-name">SNAPSHT</div>
					<div class="upload-project-input">
						<input type="file" id="snapshtFile" name="snapshtFile" class="project-snapshot"/>
					</div>
				</div>
				
				<div class="upload-project">
					<div class="upload-project-name">TITLE</div>
					<div class="upload-project-input">
						<input type="text" id="title" name='title' class="project-name" value="<c:out value='${project.title}'/>"/>
					</div>
				</div>
				
				<div class="upload-project">
					<div class="upload-project-name">DESC</div>
					<div class="upload-project-input">
						<textarea id="desc" name="desc" class="project-desc">
							<c:out value="${project.desc}" escapeXml="false"/>
						</textarea>
					</div>
				</div>
				
				<div class="upload-project">
					<div class="upload-project-name">CONTENTS</div>
					<div class="upload-project-input">
						<textarea name="contents" id="contents">
							<c:out value="${project.contents}" escapeXml="false"/>
						</textarea>
					</div>
				</div>
				
				<div class="upload-project">
					<div class="upload-project-name">FILE</div>
					<div class="upload-project-input">
						<c:import url="../included/included_fileupload.jsp" charEncoding="UTF-8">
							<c:param name="boardType" value="project"/>
						</c:import>
					</div>
				</div>
				
				
				<div class="upload-project">
					<div class="upload-project-name"></div>
					<div class="upload-project-input project-submit">
						<a class="btn" onclick="Progress.start(); history.back();">취소</a>
						<a class="btn" onclick="Progress.start(); $('#uploadForm').submit()">저장</a>
					</div>
				</div>	
				
			</form>
			
		</div>
		
		<c:import url="../included/included_footer.jsp" charEncoding="UTF-8" />
	</div>
</body>
</html>