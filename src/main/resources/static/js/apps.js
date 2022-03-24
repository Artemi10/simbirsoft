const showIcons = (event) => {
    const icons = event.target.querySelectorAll('.apps__list__table__app__action')
    icons.forEach(icon => icon.classList.remove("hidden"))
}
const hideIcons = (event) => {
    const icons = event.target.querySelectorAll('.apps__list__table__app__action')
    icons.forEach(icon => icon.classList.add("hidden"))
}

let apps = document.querySelectorAll('.apps__list__table__app')
apps.forEach(app => {
    app.addEventListener('mouseenter', showIcons)
    app.addEventListener('mouseleave', hideIcons)
})
