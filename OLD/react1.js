
class App extends React.Component{
	
	constructor(props){
		super(props);
		
		this.state={
			text:'test'
		};
		this.inputChange = this.inputChange.bind(this);
	}
	
	
	
	btnOnClick(event){
		console.log("kjhkjhkjhkjh", event.target);
	}
	
	inputChange(event){
		const text= event.target.value;
		this.setState({text});
		console.log(event.target.value);
		document.getElementById("e4").innerHTML="DONE!!!!!!!!"
	}
	
	
	render(){
		return (
		<div style={{backgroundColor: 'red'}} className="test">
		<h1 id="e4">{this.props.h1Text}</h1>
		<input type="text" value={this.state.text} onChange={this.inputChange} />
		<h3>Print h3</h3>
	<button onClick={this.btnOnClick}>{this.props.btnText}</button>
		</div>
		);
	}
	
}

ReactDOM.render(
	<App btnText="hhhhhhhhh" h1Text="222222222222"/>,
	document.getElementById("app")

);

// Трехуровневая система клиент, сервер, бд.
