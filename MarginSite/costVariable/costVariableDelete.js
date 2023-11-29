/**
 * Delete the selectionned cost
 */
function deleteCost() {
  let id = document.getElementsByClassName("buttonAdder")[0].id;
  const url = getCostUrl() + "/" + id;

  fetch(url, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
  }).then((res) => {
    if (res.ok) {
      loadFromAPI();
    } else {
      alert("Le coût n'a pas été supprimé");
    }
  });
}
