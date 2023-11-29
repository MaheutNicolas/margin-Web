/**
 * Send name, password and email to the API
 */

function sendUser() {
  let name = document.getElementById("name").value;
  let password = document.getElementById("password").value;
  let email = document.getElementById("email").value;

  if (name == "" || password == "" || email == "") return;

  fetch(getAuthUrl(), {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify({
      name: name,
      password: password,
      email: email,
    }),
  })
    .then((res) => {
      if (res.ok) {
        return res.json();
      }
    })
    .then((data) => {
      if (data) {
        switchToMargin();
      }
    });
}

/**
 * if the user is valid, switch to the programe in itself
 */
function switchToMargin() {
  window.location.href = "../costVariable/index.html";
}
