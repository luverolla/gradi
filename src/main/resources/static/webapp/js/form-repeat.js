class FormRepeat
{
    /**
     * @type HTMLFieldSetElement
     */
    fieldset;

    /**
     * @type HTMLElement
     */
    template;

    /**
     * @type number
     */
    prog = 0;

    /**
     * @type string
     */
    basename;

    /**
     * @param {HTMLFieldSetElement} e
     */
    constructor(e)
    {
        this.fieldset = e;
        this.basename = this.fieldset.dataset.fr;

        this.init();
    }

    add()
    {
        this.prog++;

        // remove button
        let rmv = document.createElement("small");
        rmv.innerText = "remove";
        rmv.setAttribute("role", "button");
        rmv.classList.add("text-danger", "d-block", "w-100", "text-lg-end");
        rmv.addEventListener("click", e => this.remove(e));

        // clone template and replace placeholder with progressive number
        let currElem = this.template.cloneNode(true);
        currElem.id = this.basename + this.prog.toString();
        currElem.innerHTML = currElem.innerHTML.replaceAll("$n", this.prog.toString());
        currElem.appendChild(rmv);

        this.fieldset.appendChild(currElem);
    }

    /**
     * @param {Event} e
     */
    remove(e)
    {
        console.log(this.fieldset, e.target, e.currentTarget);
        this.fieldset.removeChild(e.target.parentElement);
    }

    init()
    {
        let legend = this.fieldset.querySelector("legend");
        legend.classList.add("d-flex", "align-items-end");

        this.template = this.fieldset.querySelector("[data-fr-template]");
        this.template.parentElement.removeChild(this.template);

        this.add();

        // button to add
        let addBtn = document.createElement("small");
        addBtn.innerText = "add";
        addBtn.setAttribute("role", "button");
        addBtn.classList.add("ms-auto", "text-success");
        addBtn.addEventListener("click", () => this.add());
        this.fieldset.querySelector("legend").appendChild(addBtn);
    }
}

document.querySelectorAll("fieldset[data-fr]")
    .forEach(e => new FormRepeat(e));