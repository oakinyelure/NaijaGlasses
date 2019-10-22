package Core.EngineModules.Dom;

public class DomNode {
    private DomNode _parentNode;

    private String _outerLink;

    private String _textContent;

    private String _imageLink;

    private String _url;

    private String _title;

    public DomNode() {
    }

    public DomNode getParentNode() {
        return _parentNode;
    }

    public void setParentNode(DomNode _parentNode) {
        this._parentNode = _parentNode;
    }

    public String getOuterLink() {
        return _outerLink;
    }

    public void setOuterLink(String _outerLink) {
        this._outerLink = _outerLink;
    }

    public String getTextContent() {
        return _textContent;
    }

    public String getUrl() {
        return _url;
    }

    public void setUrl(String _url) {
        this._url = _url;
    }

    public void setTextContent(String _textContent) {
        this._textContent = _textContent;
    }

    public String getImageLink() {
        return _imageLink;
    }

    public void setImageLink(String _imageLink) {
        this._imageLink = _imageLink;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("href => "+this.getOuterLink());
        builder.append("img => "+this.getImageLink());
        builder.append("text => "+this.getTextContent());
        builder.append("URL => "+this.getUrl());
        builder.append("Parent => "+((this._parentNode != null) ? this._parentNode.toString() : "null"));
        return builder.toString();
    }
}
