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
    if (!data) {
      window.location.href = "/login/index.html";
    }
    loadFromAPI();
  });
