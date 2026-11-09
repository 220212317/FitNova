import { Component, type ErrorInfo, type ReactNode } from "react";

interface Props {
    children: ReactNode;
    fallback?: ReactNode;
}
interface State {
    error: Error | null;
}

export class ErrorBoundary extends Component<Props, State> {
    state: State = { error: null };

    static getDerivedStateFromError(error: Error): State {
        return { error };
    }

    componentDidCatch(error: Error, info: ErrorInfo) {
        console.error("Uncaught render error:", error, info.componentStack);
    }

    private reset = () => this.setState({ error: null });

    render() {
        if (this.state.error) {
            if (this.props.fallback) return this.props.fallback;
            return (
                <div className="empty-state" style={{ minHeight: "100vh" }}>
                    <strong>Something went wrong</strong>
                    <span style={{ maxWidth: 420 }}>
            {this.state.error.message || "An unexpected error occurred."}
          </span>
                    <div className="toolbar" style={{ marginTop: 12 }}>
                        <button className="btn btn-ghost" type="button" onClick={this.reset}>
                            Try again
                        </button>
                        <button
                            className="btn"
                            type="button"
                            onClick={() => window.location.reload()}
                        >
                            Reload page
                        </button>
                    </div>
                </div>
            );
        }
        return this.props.children;
    }
}

export default ErrorBoundary;