interface SkeletonProps {
    lines?: number;
    className?: string;
}

export function Skeleton({ lines = 3, className = "" }: SkeletonProps) {
    return (
        <div className={className} role="status" aria-label="Loading">
            {Array.from({ length: lines }).map((_, i) => (
                <div className="skeleton skeleton-row" key={i} />
            ))}
        </div>
    );
}

export default Skeleton;