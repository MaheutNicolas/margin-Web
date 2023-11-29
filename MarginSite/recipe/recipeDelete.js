/**
 * Delete the selectionned recipe
 */
function deleteRecipe() {
  let id = document.getElementsByClassName("buttonAdder")[0].id;
  const url = getRecipeUrl() + "/" + id;

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
      alert("La recette n'a pas été supprimée");
    }
  });
}
