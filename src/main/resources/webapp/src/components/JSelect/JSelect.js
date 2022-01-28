/**
 * @typedef {Object} JSelectOption
 * @property {number} key
 * @property {string} title
 * @property {string} value
 * @property {string} [group]
 * @property {boolean} selected
 * @property {boolean} disabled
 * @property {Object.<string, string>} [attributes]
 */

import React from "react"
import {Button, ListGroup} from "react-bootstrap";
import { onlyText } from "react-children-utilities";

class JSelect extends React.Component
{
    state =
    {
        shown: React.Children.toArray(this.props.children),
    }

    /**
     * @param {string} key
     */
    doSearch(key)
    {
        key = key.toLowerCase().trim();
        let _shown = React.Children.toArray(this.props.children);
        _shown = _shown.filter(e => onlyText(e.props.children).toLowerCase().trim().includes(key));

        this.setState({ shown: _shown });
    }

    /**
     *
     * @param {ReactElement} ch
     */
    renderFallbackChild(ch)
    {
        if(ch.type === "JSelect.Option")
            return (
                <option value={ch.props.value} disabled={ch.props.disabled}
                        selected={ch.props.selected}>
                    {ch.props.title}
                </option>
            );

        else if(ch.type === "JSelect.Group")
            return (
                <optgroup label={ch.props.label}>
                    {
                        React.Children.map(ch.props.children, (cch) =>
                            this.renderFallbackChild(cch)
                        )
                    }
                </optgroup>
            );

        else
            throw new Error("component type mismatch");
    }

    renderFallbackSelect()
    {
        return (
            <select name={this.props.name} id={this.props.id} required={this.props.required}
                    disabled={this.props.disabled}
            >
                {
                    React.Children.map(this.props.children, (ch) =>
                        this.renderFallbackChild(ch)
                    )
                }
            </select>
        )
    }

    render()
    {
        let children = this.props.children,
            array = React.Children.toArray(children),
            selected = array.filter(e => e.props.selected);

        return (
            <div className={"jselect position-relative"} id={"jselect__" + this.props.name}>
                <Button className={"form-control form-control-sm text-start"}>
                    { selected.join(', ') }
                </Button>
            </div>
        )
    }

    Group = class extends React.Component
    {
        render()
        {
            return (
                <>
                    <ListGroup.Item disabled={true} role={"none"}>
                        {this.props.label}
                    </ListGroup.Item>
                    {this.props.children}
                </>
            )
        }
    }

    Option = class extends React.Component
    {
        render()
        {
            let clss, vrnt;

            if(!this.props.selected && this.props.focus)
                clss = "bg-light";

            if(this.props.selected)
                vrnt = "primary";

            return (
                <ListGroup.Item className={clss} variant={vrnt} disabled={this.props.disabled}>
                    {this.props.children}
                </ListGroup.Item>
            )
        }
    }
}