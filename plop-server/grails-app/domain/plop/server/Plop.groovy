package plop.server

import groovy.transform.ToString

@ToString(includePackage = false, includes = 'text')
class Plop {

    static constraints = {
    }

    Date dateCreated
    String text

    Plop(String text) {
        this.text = text
    }
}
