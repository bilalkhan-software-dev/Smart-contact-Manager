console.log("Contact image loaded") ;
document.getElementById("image_preview").addEventListener("change", function (event) {

    let file = event.target.files[0] ;
    let reader = new FileReader() ;
    reader.onload = function (){
       let preview = document.getElementById("upload_image_preview");
       preview.src = reader.result ;
       preview.style.display = "block" ;
    } ;
    if (file) {
        reader.readAsDataURL(file);
    }
});