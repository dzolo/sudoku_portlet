/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : request.js
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

/**
 * The Request class
 * 
 * @param contextPath   A context path of the whole application
 * @return SudokuGame_Request
 */
var SudokuGame_Request = (function (contextPath)
{
    /** The context path (private property) */
    var _contextPath = null;
    
    /**
     * Sets a context path of the Request class
     * 
     * @param contextPath   A context path of the whole application
     */
    this.setContextPath = function (contextPath)
    {
        if (!contextPath || !contextPath.length)
        {
            throw new SudokuGame_NullPointerException("An empty context path");
        }
        
        _contextPath = contextPath;
    }
    
    /**
     * Gets a context path of the Request class
     * 
     * @return              A content path
     */
    this.getContextPath = function ()
    {
        if (!_contextPath || !_contextPath.length)
        {
            throw new SudokuGame_NullPointerException("An empty context path");
        }
        
        return _contextPath;
    }

    /**
     * Gets context path of the Request class
     * 
     * @return              A path for REST requests
     */
    this.getPathOfRestApp = function ()
    {
        var contextPathNoLastBackslash = this.getContextPath();
        var lastLetter = contextPathNoLastBackslash.length - 1;

        if (contextPathNoLastBackslash.substr(lastLetter) == '/')
        {
            contextPathNoLastBackslash = contextPathNoLastBackslash.substr(0, lastLetter);
        }

        return contextPathNoLastBackslash + "/rest";
    }
    
    /**
     * Makes an DELETE request to a given path.
     * The path does not contains the context path of the application. 
     *
     * @example request.makeDelete("/service/")
     * @param path          A path of a request
     * @throws SudokuGame_RequestFailedException
     */
    this.makeDelete = function (path)
    {
        $.ajax({
            url         : this.getPathOfRestApp() + path,
            async       : false,
            dataType    : 'json',
            type        : 'DELETE',
            success     : function (data, textStatus, jqXHR) {},
            error       : function (jqXHR, textStatus, et)
            {
                throw new SudokuGame_RequestFailedException(textStatus, et);
            }
        });
    }
    
    /**
     * Makes an GET request to a given path.
     * The path does not contains the context path of the application. 
     *
     * @example request.makeGet("/service/")
     * @param path          A path of a request
     * @return              A returned data object
     * @throws SudokuGame_RequestFailedException
     */
    this.makeGet = function (path)
    {
        var returnObj = null;
        
        $.ajax({
            url         : this.getPathOfRestApp() + path,
            async       : false,
            dataType    : 'json',
            type        : 'GET',
            success     : function (data, textStatus, jqXHR)
            {
                returnObj = data;
            },
            error       : function (jqXHR, textStatus, et)
            {
                throw new SudokuGame_RequestFailedException(textStatus, et);
            }
        });
        
        return returnObj;
    }
    
    /**
     * Makes an POST request to a given path.
     * The path does not contains the context path of the application. 
     *
     * @example request.makePost("/service/", {name: Ondrej, surname: Fibich})
     * @param path          A path of a request
     * @param data          An input data object of a request
     * @return              A returned data object
     * @throws SudokuGame_RequestFailedException
     */
    this.makePost = function (path, data)
    {
        var returnObj = null;
        
        $.ajax({
            url         : this.getPathOfRestApp() + path,
            async       : false,
            data        : JSON.stringify(data),
            type        : 'POST',
            dataType    : 'json',
            contentType : 'application/json',
            success     : function (data, textStatus, jqXHR)
            {
                if (data)
                {
                    returnObj = data;
                }
                else
                {
                    returnObj = {location: jqXHR.getResponseHeader('location')};
                }
            },
            error       : function (jqXHR, textStatus, et)
            {
                throw new SudokuGame_RequestFailedException(
                        textStatus + ' ' + jqXHR.status, et
                );
            }
        });
        
        return returnObj;
    }
    
    /**
     * Makes an PUT request to a given path.
     * The path does not contains the context path of the application. 
     *
     * @example request.makePut("/service/", {name: Ondrej, surname: Fibich})
     * @param path          A path of a request
     * @param inputData     An input data object of a request
     * @return              A returned data object (typically an ID of created element)
     * @throws SudokuGame_RequestFailedException
     */
    this.makePut = function (path, inputData)
    {
        var returnObj = null;
        
        $.ajax({
            url         : this.getPathOfRestApp() + path,
            async       : false,
            data        : inputData,
            dataType    : 'json',
            type        : 'PUT',
            success     : function (data, textStatus, jqXHR)
            {
                return data;
            },
            error       : function (jqXHR, textStatus, et)
            {
                throw new SudokuGame_RequestFailedException(textStatus, et);
            }
        });
        
        return returnObj;
    }
    
    // CONSTRUCT START /////////////////////////////////////////////////////////
    this.setContextPath(contextPath);
    // CONTRUCT END    /////////////////////////////////////////////////////////
    
    // returns an instance of the class
    return this;
});