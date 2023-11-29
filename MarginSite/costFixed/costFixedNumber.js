//Not in Use Part

function sendNumberOfSale() {
  let numberOfsale = document.getElementById("numberOfSale").value;
  console.log(numberOfsale);
  const url = "http://localhost:8080/api/cost/numberOfSale";
  fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      numberOfSale: numberOfsale,
    }),
    credentials: "include",
  }).then((res) => {
    if (res.ok) {
      loadNumberOfsale();
    } else {
      alert("Le nombre de vente n'a pas été modifié");
    }
  });
}
function loadNumberOfsale() {
  const url = "http://localhost:8080/api/cost/numberOfSale";
  fetch(url, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
  })
    .then((res) => {
      if (res.ok) {
        return res.json();
      }
    })
    .then((data) => {
      document.getElementById("numberOfSale").value = data;
    });
}
