function onClickDisplay(event) {
  let id = event.target.id;
  displayMenu(document.getElementById(id + "Div"), document.getElementById(id));
}

function displayMenu(menu, button) {
  if (menu.style.display == "block") {
    menu.setAttribute("style", "display:none;");
    button.setAttribute("style", "margin-bottom:10px;");
  } else {
    menu.setAttribute("style", "display:block;");
    button.setAttribute("style", "margin-bottom:0px;");
  }
}

function returnToLogin() {
  window.location.href = "../login/index.html";
}
