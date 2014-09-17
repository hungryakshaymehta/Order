var successFn = function(data) {
alert("data");
if(data == "true"){
document.getElementById("alert-box").innerHTML="Email already exists";
}
else{
document.getElementById("alert-box").innerHTML="Email valid";
}
console.debug("Success of Ajax Call");
console.debug(data);
};
var errorFn = function(err) {
console.debug("Error of ajax Call");
console.debug(err);
}
ajax1 = {
success: successFn,
error: errorFn
}
function getemail() {
var emailInput=$("#email").val();
jsRoutes.controllers.Hopes.getemail(emailInput).ajax(ajax1);
}