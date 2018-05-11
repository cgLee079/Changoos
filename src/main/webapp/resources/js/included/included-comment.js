var page;
var perPgLine;
var path;
var comtFormTemp;
var boardType;
var boardSeq;
var comtCnt;
var isAdmin;

$(document).ready(function(){
	perPgLine 	= 10;
	boardType	= $("#boardType").val();
	boardSeq	= $("#boardSeq").val();
	comtCnt		= parseInt($("#comtCnt").val());
	isAdmin		= $("#isAdmin").val();
	comtFormTemp= $(".comment-write").clone();
	path 		= getContextPath() + "/" + boardType;
	
	commentPageMove(1);
})

function br2nl(text){
	return text.replace(/(<br\s*\/?>)+/g, "\n");
}

function nl2br(text){
	return text.replace(/\n/g, "<br />");
}
function commentPageMove(pg){
	$.ajax({
		type	: "POST",
		url		: path + "/comment/paging.do",
		data	: {
			'boardSeq'	: boardSeq,					
			'page'		: pg,
			'perPgLine' : perPgLine
		},
		beforeSend : function(){
			Progress.start();
		},
		dataType: 'JSON',
		success : function(data) {
			page = pg;
			updateComment(data);
			updatePaging("commentPageMove", page, comtCnt, perPgLine, 3);
		},
		complete : function(){
			Progress.stop();
		},
		error : function(e) {
			console.log(e);
			Progress.stop();
		}
	});
}


function updatePaging(callFunc, page, comtCnt, perPgLine, pgGrpCnt){
	var boardPager	= $('.comt-pager');
	boardPager.empty();
	
	if(comtCnt > 0){
		var	pager		= drawPager(callFunc, page, comtCnt, perPgLine, pgGrpCnt);
		boardPager.append(pager);
	}
}

function makeComment(){
	var comment = "";
	comment += '<div class="comment-item">';
	comment += '<input type="hidden" class="comment-seq">';
	comment += '<input type="hidden" class="comment-boardSeq">';
	comment += '<div class="comment">';
	comment += '<a class="comment-writer"></a> <a class="comment-date"></a>'
	comment += '<div class="comment-contents editor-contents"></div>';
	comment += '</div>';
	comment += '<div class="comment-menu">';
	if(isAdmin === 'true'){
		comment += '<a onclick="addReplyForm(this)" class="btn btn-reply">답변</a>';	
	}
	if(!(isAdmin === 'true')){
		comment += '<a onclick="commentModify(this)" class="btn btn-modify">수정</a>';
	}
	comment += '<a onclick="commentDelete(this)" class="btn">삭제</a>';
	comment += '</div>';
	comment += '</div>';
	return $(comment);
}

function makeReplyComment(){
	var comment = "";
	comment += '<div class="comment-item reply">';
	comment += '<input type="hidden" class="comment-seq">';
	comment += '<input type="hidden" class="comment-boardSeq">';
	comment += '<div class="comment">';
	comment += '<a class="comment-writer"></a> <a class="comment-date"></a>'
	comment += '<div class="comment-contents editor-contents"></div>';
	comment += '</div>';
	comment += '<div class="comment-menu">';
	if(isAdmin === 'true'){
		comment += '<a onclick="commentModify(this)" class="btn btn-modify">수정</a>';
		comment += '<a onclick="commentDelete(this)" class="btn">삭제</a>';
	}
	comment += '</div>';
	comment += '</div>';
	return $(comment);
}

function updateComment(data){
	var container = $(".comments");
	var length = data.length;
	var comment;
	var item;
	
	container.empty();
	for(var i = 0; i < length; i++){
		comment = data[i];
		if(!comment.parentSeq){
			item = makeComment();
			item.find(".comment-seq").val(comment.seq);
			item.find(".comment-boardSeq").val(comment.boardSeq);
			item.find(".comment-writer").text(comment.name);
			item.find(".comment-date").text(comment.date);
			item.find(".comment-contents").html(comment.contents);
			item.appendTo(container);					
		} else{
			item = makeReplyComment();
			item.find(".comment-seq").val(comment.seq);
			item.find(".comment-boardSeq").val(comment.boardSeq);
			item.find(".comment-writer").text(comment.name);
			item.find(".comment-date").text(comment.date);
			item.find(".comment-contents").html(comment.contents);
			item.appendTo(container);	
		}
	}
}

function makeReplyForm(){
	var form = "";
	form += '<div class="comment-reply row-center">';
	form += '<img src="' + getContextPath() + '/resources/image/icon_comment_reply.png" style="width:1rem; height:1rem; margin-right: 0.3rem">';
	form += '<textarea class="comment-reply-content" id="contents" name="contents"/></textarea>';
	form += '<div onclick="doReply(this)" class="comment-reply-submit col-center">답변</div>';
	form += '</div>';
	return $(form);
}

function doReply(tg){
	var reply		= $(tg).parents(".comment-reply");
	var parentSeq	= reply.find(".parentSeq").text();
	var name		= "CHANGOO";
	var password	= "I_AM_ADMIN";
	var contents	= reply.find(".comment-reply-content").val();
	
	$.ajax({	
		type	: "POST",
		url		: path + "/comment/submit.do",
		data	: {
			'boardSeq'	: boardSeq,				
			'name'		: name,					
			'password'	: password,
			'contents' 	: nl2br(contents),
			'parentSeq' : parentSeq
		},
		dataType : "JSON",
		success : function(result) {
			commentPageMove(page);
		}
	});
}

function addReplyForm(tg){
	var tg 			= $(tg);
	var item		= $(tg).parents(".comment-item");
	var parentSeq	= item.find(".comment-seq").val();
	var form 		= makeReplyForm();

	if(tg.hasClass("open")){
		var replyForm = item.next();
		if(replyForm.hasClass("comment-reply")){
			replyForm.remove();
			tg.removeClass("open");
		}
	} else{
		$("<div>", {"class" : "parentSeq display-none", text : parentSeq}).appendTo(form);
		form.insertAfter(item);	
		tg.addClass("open");
	}
}

function doModify(tg){
	var item	= $(tg).parents(".comment-item");
	var seq 	= item.find(".comment-seq").val();
	var contents = item.find(".comment-modify #contents").val();
	contents = nl2br(contents);
	
	$.ajax({	
		type	: "POST",
		url		: path + "/comment/update.do",
		data	: {
			'seq' : seq,
			'contents' : contents
		},
		dataType : "JSON",
		success : function(result) {
			if(result) {
				commentPageMove(page);
			} else{
				swal({ text : "수정 실패하였습니다.", icon : "error" });
			}
		}
	});
}

function commentModify(tg){
	var tg = $(tg);
	if(tg.hasClass("open")){
		commentPageMove(page);
	} else {
		if(isAdmin === 'true'){
			doModify("");
		} else{
			swal({
				  text: '비밀번호를 입력해주세요',
				  content: "input",
				  buttons : ["취소", "확인"]
				})
				.then((pw) => {
					if(pw){
						doModify(pw);				  
				  	}
				});
		}
	}
	
	function doModify(password){
		var item= tg.parents(".comment-item");
		var seq	= item.find(".comment-seq").val();
		$.ajax({	
			type	: "POST",
			url		: path + "/comment/checkPwd.do",
			data	: {
				'seq' : seq,
				'password'	: password,
				'isAdmin'	: isAdmin 
			},
			dataType : "JSON",
			success : function(result) {
				if (result){
					changeToForm(item);
				} else{
					swal({
						text : "비밀번호가 틀렸습니다.", 
						icon : "error"
					});
				}
			}
		});
	}
	
	function changeToForm(item){
		var contentsDiv = item.find(".comment-contents");
		var contents = br2nl(contentsDiv.html());
		tg.toggleClass("open");
		
		var commentModify = $("<div>" , {"class" : "comment-modify"});
		$("<textarea>", {text : contents, id : "contents", "class" : "comment-write-contents"}).appendTo(commentModify);
		$("<div>", {text : "등록", onclick:  "doModify(this)", "class" : "col-center comment-write-submit"}).appendTo(commentModify);
		contentsDiv.empty();
		contentsDiv.append(commentModify);
	}
}
		
function commentDelete(tg){
	if(isAdmin === 'true'){
		swal({
			  title: "정말로 삭제 하시겠습니까?",
			  text: "삭제된 댓글은 복구 할 수 없습니다.",
			  icon: "warning",
			  buttons: ["취소", "삭제"],
			  dangerMode: true,
			})
			.then(willDelete => {
			  if (willDelete) {
				doDelete("");
			  } 
			});
	} else{
		swal({
			  	text: '비밀번호를 입력해주세요',
			  	content: "input",
				buttons : ["취소", "확인"]
			})
			.then((pw) => {
				if(pw){
					doDelete(pw);				  
			  	}
			});
	}
	
	function doDelete(password){
		var item= $(tg).parents(".comment-item");
		var seq	= item.find(".comment-seq").val();
		$.ajax({	
			type	: "POST",
			url		: path + "/comment/delete.do",
			data	: {
				'seq' : seq,
				'password' 	: password,
				'isAdmin'	: isAdmin
			},
			dataType: 'JSON',
			success : function(data) {
				if(data){
					swal({
						text : "댓글이 삭제 되었습니다.", 
						icon : "success"
					});
					
					comtCnt = comtCnt - 1;
					commentPageMove(parseInt((comtCnt-1) / perPgLine)+1);
				} else{
					swal({
						text : "비밀번호가 틀렸습니다.", 
						icon : "error"
					});
				}
			}
		});
	}	
	
	
}

function commentSubmit(){
	var name = $(".comment-write #name");
	var password = $(".comment-write #password");
	var contents  = $(".comment-write #contents");
	
	if(!name.val()) { swal({text : "이름을 입력해주세요.", icon : "warning"}); return ;}
	if(!password.val()) { swal({text : "비밀번호를 입력해주세요.", icon : "warning"}); return ;}
	if(!contents.val()) { swal({text : "내용을 입력해주세요.", icon : "warning"}); return ;}
	
	$.ajax({
		type	: "POST",
		url		: path + "/comment/submit.do",
		data	: {
			'boardSeq'	: boardSeq,				
			'name'		: name.val(),					
			'password'	: password.val(),
			'contents' 	: nl2br(contents.val())
		},
		dataType: 'JSON',
		beforeSend : function(){
			Progress.start();
		},
		success : function(data) {
			if(data){
				swal({
					text : "댓글이 등록 되었습니다.", 
					icon : "success"
				});
				contents.val('');
				comtCnt = comtCnt + 1;
				commentPageMove(parseInt((comtCnt-1) / perPgLine)+1);
			}
		},
		complete : function(){
			Progress.stop();
		},
		error : function(e) {
			console.log(e);
			swal({
				text : "댓글 등록에 실패하였습니다.", 
				icon : "error"
			});
			Progress.stop();
		}
	});
}