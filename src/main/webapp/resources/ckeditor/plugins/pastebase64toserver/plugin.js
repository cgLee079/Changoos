﻿// ckeditor-pastebase64 1.0.1r (patched)
// https://github.com/ruvor/ckeditor-pastebase64
// orig: https://github.com/javaha/ckeditor-pastebase64

(function () {
    'use strict';

    CKEDITOR.plugins.add('pastebase64toserver', {
        init: init
    });

    function init(editor) {
        if (editor.addFeature) {
            editor.addFeature({
                allowedContent: 'img[alt,id,!src]{width,height};'
            });
        }

        editor.on("contentDom", function () {
            var editableElement = editor.editable ? editor.editable() : editor.document;
            editableElement.on("paste", onPaste, null, {editor: editor});
        });
    }

    function onPaste(event) {
        var editor = event.listenerData && event.listenerData.editor;
        var $event = event.data.$;
        var clipboardData = $event.clipboardData;
        var found = false;
        var imageType = /^image/;

        if (!clipboardData) {
            return;
        }

        return Array.prototype.forEach.call(clipboardData.types, function (type, i) {
            if (found) {
                return;
            }

            if (type.match(imageType) || clipboardData.items[i].type.match(imageType)) {
            	return readImageAsBase64(clipboardData.items[i], editor, clipboardData.items.length > 1);
            }
        });
    }

    function readImageAsBase64(item, editor, useWorkAround) {
        if (!item || typeof item.getAsFile !== 'function') {
            return;
        }
        
        if(editor.config.pasteImageUrl){
	        var c = confirm("이미지를 서버에 저장하시겠습니까?");
	        if(c){
	        	var file = item.getAsFile();
	            var reader = new FileReader();
		        reader.onload = function (evt) {
		        	var base64 = evt.target.result;
		        	
		        	$.ajax({
		        		type	: "POST",
		        		url 	: editor.config.pasteImageUrl,
		        		dataType: "JSON",
		        		async 	: false,
		        		data 	: {
		        			"base64" : base64
		        		},
		        		success : function(result) {
							setTimeout(function () {
							    if (useWorkAround) {
							        var img = editor.getSelection().getRanges()[0].getBoundaryNodes().endNode;
							        if (img.$.tagName !== "IMG") {
							        img = img.getPrevious();
							    }
							    if (img && img.$.tagName === "IMG") {
							            img.remove()
							        }
							    }
							    
							    var image = {
										"editorID"	: editor.name, 
										"seq" 		: undefined,
										"pathname" 	: result.pathname,
										"filename" 	: "pasteImage.png",
								}
							    
							    imageUploader.insertCKEditor(image, imageUploader.maxWidth);
							    imageUploader.insertImageInfo(image);
							    
							}, 10);
							
							return true;
		        		}
		        	});
		        }
		        
		        reader.readAsDataURL(file);
	        } else{
	        	return;
	        }
        }
    }
})();
