/**
 * Send name and password to the API
 */
function sendUser() {
  let name = document.getElementById("loginName").value;
  let password = document.getElementById("loginPassword").value;

  fetch(getAuthUrl(), {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify({
      name: name,
      password: password,
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
  window.location.href = "/costVariable/index.html";
}

function switchToIndex() {
  window.location.href = "../index.html";
}

//verify if the user as
fetch(getAuthUrl(), {
  method: "GET",
  credentials: "include",
  headers: {
    "Content-Type": "application/json",
  },
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
